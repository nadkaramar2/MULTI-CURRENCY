import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
// import AutoPagintation from "../../UI/AutoPagintation";
export default function ViewAuthorizeDormantList() {
  const [alldata2, setAlldat2] = useState([]);

  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");
  // Function to handle changes in the search input field
  // const [itemPage, setItemPage] = useState(10);
  // const [tcount, setTcount] = useState(1);

  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata2.filter((data) => {
    const {
      accountNumber,
      requestDate,
      requestTime,
      accountType,
      dormantMarkedDate,
      requestRaisedBy,
      reasonForActive,
      requestAuthorisedBy,
      requestAuthorisedDate,
      strrequestAuthorisedTime,
      strstatus,
    } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (accountNumber &&
        accountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (requestDate &&
        requestDate.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (requestTime &&
        requestTime.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (accountType &&
        accountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (dormantMarkedDate &&
        dormantMarkedDate.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (requestRaisedBy &&
        requestRaisedBy.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (reasonForActive &&
        reasonForActive.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (requestAuthorisedBy &&
        requestAuthorisedBy.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (requestAuthorisedDate &&
        requestAuthorisedDate
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (strrequestAuthorisedTime &&
        strrequestAuthorisedTime
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (strstatus && strstatus.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
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
    handleAuthorise();
  }, []);

  const handleAuthorise = async () => {
    try {
      const response = await amsApi.post(
        `dormantToActive/getAuthorizedList`,
        {
          fromDate: "2022-09-10",
          toDate: "2023-11-16",
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setAlldat2(response.data.dormantToActiveMasters);
        // handleShowSuccess(response.data.message);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  // sorted tabled data
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
    const dateA = new Date(a.requestDate);
    const dateB = new Date(b.requestDate);
    const timeA = new Date("1970/01/01 " + a.requestTime);
    const timeB = new Date("1970/01/01 " + b.requestTime);

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
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className=" sm: px-3 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
                View Autorize Dormant List
              </p>

              <div className="flex justify-center rounded-md border border-transparent px-2 mx-2 text-sm font-medium text-white ">
                <div className="pt-2 relative mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchKeyword}
                    onChange={handleSearch}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    autoComplete="off"
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-3 mr-4"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </button>
                </div>
                {/* </div> */}
              </div>
            </div>
          </div>
        </div>

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={handleAuthorise}
        /> */}
        <div className="overflow-x-auto relative shadow-md ">
          <>
            <div className="table-wrp block max-h-[43rem] max-w-[19rem] ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="  px-3 py-3 whitespace-nowrap"
                    onClick={() => handleSort("requestDate")}
                  >
                    Request Date {renderSortArrow("requestDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("requestTime")}
                  >
                    Request Time {renderSortArrow("requestTime")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-3 whitespace-nowrap"
                    onClick={() => handleSort("accountNumber")}
                  >
                    Account Number {renderSortArrow("accountNumber")}
                  </th>

                  <th
                    scope="col"
                    className="py-3   px-3   whitespace-nowrap"
                    onClick={() => handleSort("accountType")}
                  >
                    Account Type {renderSortArrow("accountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3   whitespace-nowrap"
                    onClick={() => handleSort("dormantMarkedDate")}
                  >
                    Dormant Mark Date {renderSortArrow("dormantMarkedDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("requestRaisedBy")}
                  >
                    Request Raised By {renderSortArrow("requestRaisedBy")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("reasonForActive")}
                  >
                    Reason For Active {renderSortArrow("reasonForActive")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("requestAuthorisedBy")}
                  >
                    Request Authorised By{" "}
                    {renderSortArrow("requestAuthorisedBy")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("requestAuthorisedDate")}
                  >
                    Request authorize Date{" "}
                    {renderSortArrow("requestAuthorisedDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("requestAuthorisedReason")}
                  >
                    Request Authorize Reason{" "}
                    {renderSortArrow("requestAuthorisedReason")}
                  </th>
                  <th
                    scope="col"
                    className="py-3   px-3 whitespace-nowrap"
                    onClick={() => handleSort("status")}
                  >
                    Status {renderSortArrow("status")}
                  </th>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map(
                        ({
                          accountNumber,
                          requestDate,
                          requestTime,
                          accountType,
                          dormantMarkedDate,
                          requestRaisedBy,
                          reasonForActive,
                          requestAuthorisedBy,
                          requestAuthorisedDate,
                          requestAuthorisedReason,
                          status,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className=" border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="  px-3 py-3 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  requestDate || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  requestTime || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  accountNumber || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>

                            <td className="  px-3 py-3  whitespace-nowrap">
                              <div className="text-xs  font-medium text-gray-900">
                                {highlightKeyword(
                                  accountType || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 text-center whitespace-nowrap">
                              <div className="text-xs  font-medium text-gray-900">
                                {highlightKeyword(
                                  dormantMarkedDate || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3  whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  requestRaisedBy || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  reasonForActive || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  requestAuthorisedBy || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 text-center whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  requestAuthorisedDate || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3  whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  requestAuthorisedReason || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-3 py-3 whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(status || "-", searchKeyword)}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className=" px-3 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500 ">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
