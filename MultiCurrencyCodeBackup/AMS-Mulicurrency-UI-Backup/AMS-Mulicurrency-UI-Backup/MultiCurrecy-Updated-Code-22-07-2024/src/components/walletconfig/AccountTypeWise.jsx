import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function AccountTypeWise() {
  const [alldata, setAlldata] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [selectdata, setSelectdata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const [error, setError] = useState("");
  const [blockdata, setBlockdata] = useState([]);

  const [searchQuery1, setSearchQuery1] = useState("");

  const [checkpoint1, setCheckpoint1] = useState(0);

  const [searchKeyword, setSearchKeyword] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);
  const [itemPage1, setItemPage1] = useState(10);
  const [tcount1, setTcount1] = useState(0);
  let userid = localStorage.getItem("userName");

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

  // seach

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };

  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.get(`accountType/getAccntType`, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // useEffect(() => {
  //   Blockmcc();
  // }, []);
  const Blockmcc = async () => {
    try {
      const response = await amsApi.post(
        `account_type_wallet/accountTypBasedWallet`,
        {
          strAccounType: accounttype,
          strParticipantID: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setBlockdata(response.data);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  // Selected mcc
  // useEffect(() => {
  //   Selected();
  // }, []);
  useEffect(() => {
    Selected();
    setError("");
  }, []);

  const Selected = async () => {
    if (accounttype === "" || accounttype.length === 0) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID");
    } else {
      try {
        const response = await amsApi.post(
          `participant_wallet/getParticpantBasedMcc`,
          {
            strAccounType: accounttype,
            strParticipantID: participantID,
          }
        );
        if (response.data.code === "S0000") {
          setSelectdata(response.data);
          setCheckpoint1(1);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const [selectedRows, setSelectedRows] = useState([]);

  // Update selectedRows array when a checkbox is clicked
  const handleCheckboxChange = (e, rowId) => {
    const isChecked = e.target.checked;
    if (isChecked) {
      setSelectedRows([...selectedRows, rowId]);
    } else {
      setSelectedRows(selectedRows.filter((id) => id !== rowId));
    }
  };

  const [percentage, setpercentage] = useState("");
  const handleInputChange = (e) => {
    const { value } = e.target;
    const formattedValue = value; // Append percentage sign
    setpercentage(formattedValue);
  };

  // check box with addsave button

  const handleSubmit = async (selectedRows) => {
    // event.preventDefault();
    const selectedMccCodes = selectedRows.join(",");
    if (
      selectedMccCodes === "" ||
      selectedMccCodes.length === 0 ||
      percentage === ""
    ) {
      swal("Please Select  MCC Code (check box) and Percentage");
    } else if (!accounttype) {
      swal("Please Select Account Type ");
    } else if (!participantID || !userid) {
      swal("Please check your userid and participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `account_type_wallet/addAccountTypWiseWallet`,
          {
            strMccCode: selectedMccCodes,
            strParticipantID: participantID,
            strCreatedBy: userid,
            strAccounType: accounttype,
            strPercentage: percentage,
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          Blockmcc();
          setAccountType("");
          setSelectedRows([]);
          // swal("Wallet Added Successfully for Participant !");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };
  const handleClick = () => {
    setAccountType("");
    setSelectedRows([]);
  };

  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const renderSortArrow = (column) => {
    if (column === sortColumn) {
      return sortOrder === "asc" ? "↑" : "↓";
    }
    return "↕";
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...selectdata].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  // list
  const [sortColumn1, setSortColumn1] = useState(""); // State to track the sorted column
  const [sortOrder1, setSortOrder1] = useState("asc"); // State to track sorting order

  const handleSort1 = (column) => {
    if (column === sortColumn1) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder1(sortOrder1 === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn1(column);
      setSortOrder1("asc");
    }
  };

  const renderSortArrow1 = (column) => {
    if (column === sortColumn1) {
      return sortOrder1 === "asc" ? "↑" : "↓";
    }
    return "↕";
  };

  // Sort the data based on the chosen column and order
  const sortedData1 = [...blockdata].sort((a, b) => {
    if (sortOrder1 === "asc") {
      const aValue = a[sortColumn1] || "";
      const bValue = b[sortColumn1] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn1] || "";
      const bValue = b[sortColumn1] || "";
      return bValue.localeCompare(aValue);
    }
  });
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Account Type Wise MCC Configuration For Wallet
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-2 sm:p-2 bg-white">
                <div className="grid gap-6  md:grid-cols-3 px-8">
                  <div>
                    <label
                      htmlFor="Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>

                    <select
                      name="accounttype"
                      id="accounttype"
                      value={accounttype}
                      // onChange={(e) => setAccountType(e.target.value, Blockmcc())}
                      onChange={(e) => setAccountType(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      <option value="">Select</option>

                      {alldata.map((data) => (
                        <option value={data.strAccountType}>
                          {data.strAccountType}-{data.strDescription}
                        </option>
                      ))}
                    </select>
                    {error && accounttype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please select Account Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex justify-between items-end px-4 py-2 mt-2 text-right sm:px-2 ">
                    <button
                      type="button"
                      onClick={Selected}
                      data-modal-toggle="defaultModal"
                      className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5 mx-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                    >
                      Search
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>

        <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200 mt-2 ">
          <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
            SELECT MCC FOR BLOCK
          </p>

          <div className="flex justify-center rounded-md border border-transparent px-2 mx-2 text-xs font-medium text-white ">
            <div className="pt-2 relative mx-auto text-gray-600">
              <input
                title="Search Data"
                type="search"
                id="search"
                className=" block font-normal py-1 text-gray-700 text-sm bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 px-2 "
                placeholder="Search... "
                // value={searchKeyword}
                // onChange={handleSearch}
                // style={{
                //   backgroundColor: checkpoint1 === 1 ? "white" : "lightgray",
                // }}
                // disabled={checkpoint1 !== 1}
              />
              <button
                type="submit"
                className="absolute right-0 top-0 mt-4 mr-2"
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
          </div>
        </div>
        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={selectdata[0]?.strTotalCount}
          reCallApi={Selected}
        />
        <div className="overflow-x-auto relative shadow-md  ">
          {/* {selectdata.length > 0 && (
          <> */}
          <div className="table-wrp block max-h-[15rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="px-6 py-2.5"></th>
                  <th
                    scope="col"
                    className="py-2.5 px-8"
                    onClick={() => handleSort("strMccCode")}
                  >
                    MCC {renderSortArrow("strMccCode")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6"
                    onClick={() => handleSort("strMccCodeDesc")}
                  >
                    DESCRIPTION {renderSortArrow("strMccCodeDesc")}
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    Percentage
                  </th>
                </tr>
              </thead>
              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map(({ strMccCode, strMccCodeDesc }) => (
                      <tr className="border-b dark:border-neutral-500">
                        <td className="whitespace-nowrap px-6 py-2.5">
                          <div className="form-control">
                            <label className="cursor-pointer label">
                              <input
                                type="checkbox"
                                value={strMccCode}
                                onChange={(e) =>
                                  handleCheckboxChange(e, strMccCode)
                                } // Pass the unique identifier to the handler
                                checked={selectedRows.includes(strMccCode)} // Check if the row is selected
                                className="checkbox checkbox-info  checkbox-sm"
                              />
                            </label>
                          </div>
                        </td>
                        <td className="whitespace-nowrap px-6 py-2.5">
                          <span className="text-xs font-medium text-gray-900">
                            {strMccCode || "-"}
                          </span>
                        </td>
                        <td className="whitespace-nowrap px-6 py-2.5">
                          <span className="text-xs font-medium text-gray-900">
                            {strMccCodeDesc || "-"}
                          </span>
                        </td>
                        <td className="whitespace-nowrap px-6 py-2.5">
                          <div className="flex ">
                            <input
                              type="text"
                              name="percentage"
                              // value={percentage}
                              onChange={(event) => handleInputChange(event)}
                              className="block w-36 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded-l transition ease-in-out m-0"
                            />
                            <div className="">
                              <span className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded-r transition ease-in-out m-0">
                                %
                              </span>
                            </div>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-1 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
          {/* </>
        )} */}
        </div>
        <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
          <button
            title="Click Submit Button"
            type="button"
            data-modal-toggle="defaultModal"
            onClick={() => handleSubmit(selectedRows)} // Pass the selectedRows array
            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3 mx-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
          >
            Submit
          </button>
          <button
            title="Clear Data"
            type="button"
            onClick={handleClick}
            className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-3 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Clear
          </button>
        </div>
        <div className="overflow-x-auto relative shadow-md mt-2 ">
          {/* {blockdata.length > 0 && ( */}
          <>
            <div className="flex justify-between items-center sm:px-2 lg:px-8 bg-blue-200   ">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                BLOCKED MCC ACCOUNT TYPE WISE
              </p>
              <div className=" flex justify-center px-2 mx-2 font-medium    ">
                <div className="pt-2 relative mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchQuery1}
                    onChange={(e) => setSearchQuery1(e.target.value)}
                    className=" block font-normal text-gray-700 text-base bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 px-2 py-1"
                    placeholder="Search... "
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-4 mr-2"
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
              </div>
            </div>
            <AutoPagintation
              addPageperData={setItemPage1}
              addCurrentPage={setTcount1}
              pagePerData={itemPage1}
              currentPage={tcount1}
              tcount={blockdata[0]?.strTotalCount}
              reCallApi={Blockmcc}
            />

            <div className="overflow-x-auto relative shadow-md  ">
              <div className="table-wrp block max-h-[20rem] ">
                <table className="w-full text-xs text-left text-black dark:text-blue-100">
                  <thead className="border-b text-xs sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                    <th
                      scope="col"
                      className="py-2.5 px-8"
                      onClick={() => handleSort1("strMccCode")}
                    >
                      MCC {renderSortArrow1("strMccCode")}
                    </th>
                    <th
                      scope="col"
                      className="py-2.5 px-6"
                      onClick={() => handleSort1("strMccCodeDesc")}
                    >
                      DESCRIPTION {renderSortArrow1("strMccCodeDesc")}
                    </th>
                  </thead>
                  <tbody>
                    {sortedData1.length > 0 ? (
                      <>
                        {sortedData1
                          .filter(
                            (data) =>
                              data.strMccCode

                                .toLowerCase()
                                .includes(searchQuery1.toLowerCase()) ||
                              data.strMccCodeDesc
                                .toLowerCase()
                                .includes(searchQuery1.toLowerCase())
                          )
                          .map(({ strMccCode, strMccCodeDesc }) => (
                            <tr
                              key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className="whitespace-nowrap px-6 py-2.5">
                                <span className="text-xs font-medium text-gray-900">
                                  {strMccCode || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-6 py-2.5">
                                <span className="text-xs font-medium text-gray-900">
                                  {strMccCodeDesc || "-"}
                                </span>
                              </td>
                            </tr>
                          ))}
                      </>
                    ) : (
                      <tr>
                        <td colSpan="6" className="px-6 py-1.5 text-center">
                          <span className="text-sm font-normal text-gray-500">
                            No data available.
                          </span>
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </>
          {/* )} */}
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage1} tCountData={setTcount1} /> */}

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
