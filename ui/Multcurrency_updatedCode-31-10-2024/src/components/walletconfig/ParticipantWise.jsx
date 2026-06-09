import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";

export default function ParticipantWise() {
  const [alldata, setAlldata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  let userid = localStorage.getItem("userName");
  const [searchQuery, setSearchQuery] = useState("");
  const [searchQuery1, setSearchQuery1] = useState("");

  const [checkpoint, setCheckpoint] = useState(0);
  const [checkpoint1, setCheckpoint1] = useState(0);

  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
  const [itemPage1, setItemPage1] = useState(20);
  const [tcount1, setTcount1] = useState(1);

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
    UnSelected();
  }, [tcount, itemPage]);

  const UnSelected = async () => {
    try {
      const response = await amsApi.post(
        `mcc_code/getUnSelectedallMCC/pagination/${itemPage}/${tcount}`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.mccListData);

       
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowSuccess(error.response.data.message);
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       UnSelected();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `mcc_code/getUnSelectedallMCCSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setAlldata(response.data.unSelectMccSearchList);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
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

  // save
  const AddP = async (selectedRows) => {
    const selectedMccCodes = selectedRows.join(",");
    if (!selectedMccCodes) {
      swal("Please Select Mcc Code");
    } else if (!participantID || !userid) {
      swal("Please Check your Userid and participantID");
    } else {
      try {
        const response = await amsApi.post(`participant_wallet/add`, {
          strMccCode: selectedMccCodes,
          strParticipantID: participantID,
          strCreatedBy: userid,
        });

        if (response.status === 200) {
          handleShowSuccess("Wallet Added Successfully for Participant !");
          Selected();
          setSelectedRows([]);
        } else {
          handleShowError("Wallet Not Added  for Participant");
        }
      } catch (error) {
        handleShowError("Wallet Not Added  for Participant");
      }
    }
  };

  // selected mcc data

  const [list, setList] = useState([]);
  useEffect(() => {
    Selected();
  }, [tcount1, itemPage1]);
  const Selected = async () => {
    try {
      const response = await amsApi.post(
        `mcc_code/getSelectMCCList/pagination/${itemPage1}/${tcount1}`,
        {
          strParticipantID: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setList(response.data.mccListData);
      } else {
        //  handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };

  const handleClick = () => {
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
  const sortedData = [...alldata].sort((a, b) => {
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

  const [sortColumn1, setSortColumn1] = useState(""); // State to track the sorted column
  const [sortOrder1, setSortOrder1] = useState("asc"); // State to track sorting order

  const handleSort1 = (column) => {
    if (column === sortColumn1) {
      setSortOrder1(sortOrder1 === "asc" ? "desc" : "asc");
    } else {
      setSortOrder1("asc");
    }
  };

  const renderSortArrow1 = (column) => {
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

  // Sort the data based on the chosen column and order
  const sortedData1 = [...list].sort((a, b) => {
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
      <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[37rem]">
        <div className="max-w-full mx-auto h-full sticky top-0 ">
          <div className="min-h-full  first-line:flex items-center justify-center ">
            <div className="flex justify-between items-center sm:px-6 lg:px-8 sticky top-0 bg-blue-200   py-1 mt-2 rounded-t-lg ">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal  ">
                UNSELECTED MCC FOR PARTICIPANT
              </p>

              {/* <div className="flex justify-center rounded-md border border-transparent px-2  text-sm font-medium text-white ">
                <div className=" relative mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-2 mr-2 mb-2"
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
              </div> */}
            </div>
            <AutoPagintationReport
              addPageperData={setItemPage}
              addCurrentPage={setTcount}
              searchData={setSearchQuery}
              pagePerData={itemPage}
              currentPage={tcount}
              tcount={alldata[0]?.strTotalCount}
              reCallApi={Search}
            />
            {/* <AutoPagintation
              addPageperData={setItemPage}
              addCurrentPage={setTcount}
              pagePerData={itemPage}
              currentPage={tcount}
              tcount={sortedData[0]?.strTotalCount}
              reCallApi={UnSelected}
            /> */}

            <div className="overflow-x-auto relative shadow-md ">
              {/* {alldata.length > 0 && (
          <> */}
              <div className="table-wrp block max-h-[20rem] ">
                <table className="w-full text-xs text-left text-black dark:text-blue-100">
                  <thead className="border-b text-xs sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                    <tr>
                      <th scope="col" className="px-6 py-2.5"></th>
                      <th
                        scope="col"
                        className="px-6 py-2.5"
                        onClick={() => handleSort("strMccCode")}
                      >
                        MCC {renderSortArrow("strMccCode")}
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-2.5 "
                        onClick={() => handleSort("strMccCodeDesc")}
                      >
                        DESCRIPTION {renderSortArrow("strMccCodeDesc")}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {sortedData.length > 0 ? (
                      <>
                        {sortedData
                          
                          .map(({ strMccCode, strMccCodeDesc }) => (
                            <tr
                              key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className="whitespace-nowrap px-6 py-2.5">
                                <div className="form-control">
                                  <label className="cursor-pointer label">
                                    <input
                                      type="checkbox"
                                      value={strMccCode}
                                      onChange={(e) =>
                                        handleCheckboxChange(e, strMccCode)
                                      } // Pass the unique identifier to the handler
                                      checked={selectedRows.includes(
                                        strMccCode
                                      )} // Check if the row is selected
                                      className="checkbox checkbox-info  checkbox-sm"
                                    />
                                  </label>
                                </div>
                              </td>
                              <td className="whitespace-nowrap px-6 py-2.5">
                                {""}
                                <span className="text-sm font-medium text-gray-900">
                                  {strMccCode || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-6 py-2.5">
                                {""}
                                <span className="text-sm font-medium text-gray-900">
                                  {strMccCodeDesc || "-"}
                                </span>
                              </td>
                            </tr>
                          ))}
                      </>
                    ) : (
                      <tr>
                        <td colSpan="6" className="px-6 py-2.5 text-center">
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
          {/* </>
        )} */}
        </div>

        <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
          <button
            title="Click Submit Button"
            type="button"
            data-modal-toggle="defaultModal"
            onClick={() => AddP(selectedRows)} // Pass the selectedRows array
            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-4 mx-2  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
          >
            Submit
          </button>
          <button
            title="Clear Data"
            type="button"
            onClick={handleClick}
            className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-4 mx-2 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Clear
          </button>
        </div>

        <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200   mt-2 rounded-t-lg ">
          <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
            {""}
            LIST OF SELECTED MCC BY PARTICIPANT
          </p>
        </div>
        <AutoPagintationReport
          addPageperData={setItemPage1}
          addCurrentPage={setTcount1}
          searchData={setSearchQuery1}
          pagePerData={itemPage1}
          currentPage={tcount1}
          tcount={sortedData1[0]?.strTotalCount}
          reCallApi={UnSelected}
        />
        {/* <AutoPagintation
          addPageperData={setItemPage1}
          addCurrentPage={setTcount1}
          pagePerData={itemPage1}
          currentPage={tcount1}
          tcount={list[0]?.strTotalCount}
          reCallApi={Selected}
        /> */}
        {/* table */}

        <div className="overflow-x-auto relative shadow-md  ">
          {/* {list.length > 0 && (
          <> */}
          <div className="table-wrp block max-h-[20rem] ">
            <table className="w-full text-sm text-left text-black dark:text-blue-100">
              <thead className="border-b text-xs sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th
                    scope="col"
                    className="px-6 py-2.5"
                    onClick={() => handleSort1("strMccCode")}
                  >
                    MCC {renderSortArrow1("strMccCode")}
                  </th>
                  <th
                    scope="col"
                    className="px-6 py-2.5"
                    onClick={() => handleSort1("strMccCodeDesc")}
                  >
                    DESCRIPTION {renderSortArrow1("strMccCodeDesc")}
                  </th>
                </tr>
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
                      .map((data) => (
                        <tr
                          key={uuidv4()}
                          className="border-b dark:border-neutral-500"
                        >
                          <td className="whitespace-nowrap px-6 py-2.5">
                            {""}
                            <span className="text-sm font-medium text-gray-900">
                              {data.strMccCode || "-"}
                            </span>
                          </td>
                          <td className="whitespace-nowrap px-6 py-2.5">
                            {""}
                            <span className="text-sm font-medium text-gray-900">
                              {data.strMccCodeDesc || "-"}
                            </span>
                          </td>
                        </tr>
                      ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-2.5 text-center">
                      <span className="text-lg font-medium text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          {/* </>
        )} */}
        </div>
      </div>

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
