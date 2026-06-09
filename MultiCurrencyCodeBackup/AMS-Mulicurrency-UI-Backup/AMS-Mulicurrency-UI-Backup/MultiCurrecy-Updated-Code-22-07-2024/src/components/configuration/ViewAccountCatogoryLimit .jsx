import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { LiaUserEditSolid } from "react-icons/lia";

export default function ViewAccountCatogoryLimit() {
  const [alldata, setAlldata] = useState([]);
  const [error, setError] = useState("");

  let participantID = sessionStorage.getItem("Participantid");
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");

  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };

  const filteredData = alldata.filter((data) => {
    const {
      strCreditType,
      purchaseCreditLimit,
      purchasePerTxnLimit,
      cashWdlCreditLimit,
      cashWdlPerTxnLimit,
      purchaseTxnPerDayCount,
      purchaseTxnPerMonthCount,
      cashWdlTxnPerDayCount,
      cashWdlTxnPerMonthCount,
      strCreatedBy,
      strCreatedDate,
    } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();
    return (
      (strCreditType &&
        strCreditType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      ((typeof purchaseCreditLimit === "string" ||
        typeof purchaseCreditLimit === "number") &&
        purchaseCreditLimit
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof purchasePerTxnLimit === "string" ||
        typeof purchasePerTxnLimit === "number") &&
        purchasePerTxnLimit
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof cashWdlCreditLimit === "string" ||
        typeof cashWdlCreditLimit === "number") &&
        cashWdlCreditLimit
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof cashWdlPerTxnLimit === "string" ||
        typeof cashWdlPerTxnLimit === "number") &&
        cashWdlPerTxnLimit
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof purchaseTxnPerDayCount === "string" ||
        typeof purchaseTxnPerDayCount === "number") &&
        purchaseTxnPerDayCount
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof purchaseTxnPerMonthCount === "string" ||
        typeof purchaseTxnPerMonthCount === "number") &&
        purchaseTxnPerMonthCount
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof cashWdlTxnPerDayCount === "string" ||
        typeof cashWdlTxnPerDayCount === "number") &&
        cashWdlTxnPerDayCount
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      ((typeof cashWdlTxnPerMonthCount === "string" ||
        typeof cashWdlTxnPerMonthCount === "number") &&
        cashWdlTxnPerMonthCount
          .toString()
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (strCreatedBy &&
        strCreatedBy.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strCreatedDate &&
        strCreatedDate.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text;
    }
    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  };

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  useEffect(() => {
    tabledata();
  }, []);
  const tabledata = async () => {
    try {
      const response = await amsApi.post(
        `credit_limit/getCreditLimitListByParticpantWise`,
        {
          strParticipantId: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountCreditLimitCategoriesList);
        setCheckpoint(1);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  const [editingRow, setEditingRow] = useState(null);
  const startEdit = (txnId) => {
    setEditingRow(txnId);
  };
  const handleEditChange = (e, strID, field) => {
    const updatedData = alldata.map((data) => {
      if (data.strID === strID) {
        return { ...data, [field]: e.target.value };
      }
      return data;
    });
    setAlldata(updatedData);
  };

  const handleUpdate = async (strID) => {
    try {
      const updatedRow = alldata.find((data) => data.strID === strID);
      const response = await amsApi.post(
        `credit_limit/updateCrditTypeCategoryLimit`,
        {
          strCreditType: updatedRow.strCreditType,
          purchaseCreditLimit: updatedRow.purchaseCreditLimit,
          purchasePerTxnLimit: updatedRow.purchasePerTxnLimit,
          cashWdlCreditLimit: updatedRow.cashWdlCreditLimit,
          cashWdlPerTxnLimit: updatedRow.cashWdlPerTxnLimit,
          purchaseTxnPerDayCount: updatedRow.purchaseTxnPerDayCount,
          purchaseTxnPerMonthCount: updatedRow.purchaseTxnPerMonthCount,
          cashWdlTxnPerDayCount: updatedRow.cashWdlTxnPerDayCount,
          cashWdlTxnPerMonthCount: updatedRow.cashWdlTxnPerMonthCount,
        },
        {
          headers: {
            "Content-type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        handleShowSuccess(response.data.message);
        setEditingRow(null);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  const cancelEdit = () => {
    setEditingRow(null);
  };

  // Sorted Tabled data by Date and time wise
  const [sortOrder, setSortOrder] = useState("asc");
  const [sortColumn, setSortColumn] = useState("");
  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const sortedData = filteredData.slice(0).sort((a, b) => {
    const dateA = new Date(a.strCreatedDate);
    const dateB = new Date(b.strCreatedDate);
    const timeA = new Date("1970/01/01 " + a.strCreatedDate);
    const timeB = new Date("1970/01/01 " + b.strCreatedDate);
    if (dateA.getTime() === dateB.getTime()) {
      return sortOrder === "asc" ? timeA - timeB : timeB - timeA;
    } else {
      return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    }
  });
  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  return (
    <AppLayout>
      <div className="max-w-7x1 mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-300">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Account Catogory Limit
              </p>
              <div className=" flex justify-center  rounded-b-md border border-transparent  px-5 mx-4 text-xs  font-medium text-white ">
                <div className="relative mx-auto text-gray-600">
                  <span
                    type="submit"
                    className="absolute left-0 top-0 mt-1 px-1 "
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-5 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </span>
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchKeyword}
                    onChange={handleSearch}
                    className="block w-full pl-10 pr-1 py-1 text-sm font-normal text-black-700 bg-white bg-clip-padding border-b-2 border-black rounded transition ease-in-out m-0"
                    placeholder="Search"
                    autoComplete="off"
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="overflow-x-auto relative shadow-md mt-4 ">
          <div className="table-wrp block max-h-[27rem] max-w-[10rem]  ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100">
              {/* <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white"> */}
              <thead className="text-xs border-b-2 sticky top-0 text-slate-500 ">
                <tr>
                  <th
                    scope="col"
                    className="py-2.5 px-2  whitespace-nowrap"
                    // onClick={() => handleSort("strTxnId")}
                  >
                    Action
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("strCreatedDate")}
                  >
                    Created Date{renderSortArrow("strCreatedDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("strCreditType")}
                  >
                    Credit Type{renderSortArrow("strCreditType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("purchaseCreditLimit")}
                  >
                    Purchase Credit Limit
                    {renderSortArrow("purchaseCreditLimit")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("purchasePerTxnLimit")}
                  >
                    Purchase Per Txn Limit
                    {renderSortArrow("purchasePerTxnLimit")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("cashWdlCreditLimit")}
                  >
                    Cash Wdl Credit Limit{renderSortArrow("cashWdlCreditLimit")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("cashWdlPerTxnLimit")}
                  >
                    Cash Wdl PerTxn Limit{renderSortArrow("cashWdlPerTxnLimit")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("purchaseTxnPerDayCount")}
                  >
                    Purchase Txn PerDay Count
                    {renderSortArrow("purchaseTxnPerDayCount")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("purchaseTxnPerMonthCount")}
                  >
                    Purchase Txn PerMonth Count
                    {renderSortArrow("purchaseTxnPerMonthCount")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("cashWdlTxnPerDayCount")}
                  >
                    Cash Wdl Txn PerDay Count
                    {renderSortArrow("cashWdlTxnPerDayCount")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-2 whitespace-nowrap"
                    onClick={() => handleSort("cashWdlTxnPerMonthCount")}
                  >
                    Cash Wdl Txn PerMonth Count
                    {renderSortArrow("cashWdlTxnPerMonthCount")}
                  </th>
                  <th scope="col" className="py-2.5 px-2 whitespace-nowrap">
                    Created By
                  </th>
                </tr>
              </thead>

              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map(
                      ({
                        strID,
                        strCreatedDate,
                        strCreditType,
                        purchaseCreditLimit,
                        purchasePerTxnLimit,
                        cashWdlCreditLimit,
                        cashWdlPerTxnLimit,
                        purchaseTxnPerDayCount,
                        purchaseTxnPerMonthCount,
                        cashWdlTxnPerDayCount,
                        cashWdlTxnPerMonthCount,
                        strCreatedBy,
                      }) => (
                        <tr
                          key={strID}
                          className="border-b dark:border-neutral-500"
                        >
                          <td className="px-1 py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <div>
                                <button
                                  title="Click Update Button"
                                  className="inline-flex justify-center rounded-md border border-transparent bg-cyan-700 py-1 px-2  text-xs font-medium text-white shadow-sm hover:bg-cyan-700 focus:outline-none focus:ring-2 focus:ring-cyan-500 focus:ring-offset-2"
                                  onClick={() => handleUpdate(strID)}
                                >
                                  Update
                                </button>
                                <button
                                  title="Click Cancel Button"
                                  className="inline-flex justify-center rounded-md border border-transparent mx-1 bg-rose-500 py-1 px-2  text-xs font-medium text-white shadow-sm hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
                                  onClick={() => cancelEdit()}
                                >
                                  Cancel
                                </button>
                              </div>
                            ) : (
                              <button
                                title="Click Edit Button"
                                type="button"
                                onClick={() => startEdit(strID)}
                                className="px-2 py-1 whitespace-nowrap"
                              >
                                <LiaUserEditSolid className="w-6 h-6 text-blue-400 text-bold" />
                                {/* <svg
                                  xmlns="http://www.w3.org/2000/svg"
                                  fill="none"
                                  viewBox="0 0 24 24"
                                  strokeWidth={1.5}
                                  stroke="blue"
                                  className="w-5 h-5"
                                >
                                  <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                                  />
                                </svg> */}
                              </button>
                            )}
                          </td>
                          <td className="px-2   py-1 whitespace-nowrap">
                            <div className="text-xs text-gray-900">
                              {highlightKeyword(
                                strCreatedDate || "",
                                searchKeyword
                              )}
                            </div>
                          </td>

                          <td className="px-2   py-1 whitespace-nowrap">
                            {/* {editingRow === strID ? (
                              <input
                                type="text"
                                value={strCreditType}
                                onChange={(e) =>
                                  handleEditChange(e, strID, "strCreditType")
                                }
                                className=" border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : ( */}
                            <div className="text-xs text-gray-900">
                              {highlightKeyword(
                                strCreditType || "",
                                searchKeyword
                              )}
                            </div>
                            {/* )} */}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={purchaseCreditLimit}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "purchaseCreditLimit"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  purchaseCreditLimit || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={purchasePerTxnLimit}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "purchasePerTxnLimit"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  purchasePerTxnLimit || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={cashWdlCreditLimit}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "cashWdlCreditLimit"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  cashWdlCreditLimit || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={cashWdlPerTxnLimit}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "cashWdlPerTxnLimit"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  cashWdlPerTxnLimit || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={purchaseTxnPerDayCount}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "purchaseTxnPerDayCount"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  purchaseTxnPerDayCount || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={purchaseTxnPerMonthCount}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "purchaseTxnPerMonthCount"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  purchaseTxnPerMonthCount || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={cashWdlTxnPerDayCount}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "cashWdlTxnPerDayCount"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  cashWdlTxnPerDayCount || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2  text-end py-1 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={cashWdlTxnPerMonthCount}
                                onChange={(e) =>
                                  handleEditChange(
                                    e,
                                    strID,
                                    "cashWdlTxnPerMonthCount"
                                  )
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-md focus:ring-blue-500 focus:border-blue-500 block w-full p-1 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xs text-end text-gray-900">
                                {highlightKeyword(
                                  cashWdlTxnPerMonthCount || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-2 py-1 whitespace-nowrap">
                            <div className="text-xs text-gray-900">
                              {highlightKeyword(
                                strCreatedBy || "",
                                searchKeyword
                              )}
                            </div>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <tr>
                    <td colSpan="12" className="px-2 py-1 text-center">
                      <span className="text-lg font-medium text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
